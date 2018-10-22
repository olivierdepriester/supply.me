import { Component, OnInit, HostListener, ElementRef, ViewChild, forwardRef, Renderer2 } from '@angular/core';
import { TreeNode } from 'primeng/components/common/api';
import { MaterialCategoryService } from 'app/entities/material-category';
import { IMaterialCategory } from 'app/shared/model/material-category.model';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

@Component({
    selector: 'jhi-category-selector',
    templateUrl: './category-selector.component.html',
    styleUrls: ['./category-selector.scss'],
    providers: [
        {
            provide: NG_VALUE_ACCESSOR,
            useExisting: forwardRef(() => CategorySelectorComponent),
            multi: true
        }
    ]
})
export class CategorySelectorComponent implements OnInit, ControlValueAccessor {
    categoriesTree: TreeNode[];
    private _filterQuery: string;
    displayTree = false;
    selectedData: IMaterialCategory;
    selectedCategoryTreeNode: TreeNode;
    @ViewChild('container', { read: ElementRef })
    container: ElementRef;
    private internalCategoriesTree: TreeNode[];

    private treeNodeList: TreeNode[];
    constructor(private renderer: Renderer2, private service: MaterialCategoryService) {}

    ngOnInit() {
        this.service.query().subscribe(res => this.buildCategoryTreeNode(res.body));
    }

    @HostListener('document:click', ['$event.target'])
    onOutClick(targetElement) {
        const clickedInside = this.container.nativeElement.contains(targetElement);
        if (!clickedInside) {
            if (this.displayTree) {
                this.hide();
            }
        }
    }

    get filterQuery(): string {
        return this._filterQuery;
    }

    set filterQuery(value: string) {
        this._filterQuery = value;
        this.search();
        this.show();
    }

    get selectedCategoriesId(): number[] {
        const mainNode = this.treeNodeList.find(node => node.data.id === this.selectedCategoryTreeNode.data.id);
        return this.getAllChildren(mainNode)
            .concat(mainNode)
            .map(node => node.data.id);
    }

    private getAllChildren(node: TreeNode): TreeNode[] {
        const result: TreeNode[] = [];
        if (node.children) {
            node.children.forEach(childNode => {
                result.push(childNode);
                this.getAllChildren(childNode).forEach(subNode => result.push(subNode));
            });
        }
        return result;
    }

    onInputKeyDown(event, treeField) {
        if (this.displayTree) {
            switch (event.which) {
                // case 9: // Tab
                case 27: // Escape
                    this.hide();
                    event.preventDefault();
                    break;
                case 13:
                    this.nodeSelect(this.selectedCategoryTreeNode);
                    break;
                case 37:
                    if (this.selectedCategoryTreeNode != null) {
                        if (this.selectedCategoryTreeNode.expanded) {
                            this.selectedCategoryTreeNode.expanded = false;
                        }
                        event.preventDefault();
                    }
                    break;
                case 38: // Up
                    if (this.selectedCategoryTreeNode != null) {
                        this.selectedCategoryTreeNode = this.getPrevious(this.selectedCategoryTreeNode);
                    }
                    break;
                case 39:
                    if (this.selectedCategoryTreeNode != null) {
                        if (!this.selectedCategoryTreeNode.expanded) {
                            this.selectedCategoryTreeNode.expanded = true;
                        }
                        event.preventDefault();
                    }
                    break;
                case 40: // Down
                    if (this.selectedCategoryTreeNode == null) {
                        this.selectedCategoryTreeNode = this.categoriesTree[0];
                    } else {
                        if (this.selectedCategoryTreeNode.expanded && this.selectedCategoryTreeNode.children.length > 0) {
                            this.selectedCategoryTreeNode = this.selectedCategoryTreeNode.children[0];
                        } else {
                            this.selectedCategoryTreeNode = this.getNext(this.selectedCategoryTreeNode);
                        }
                    }
                    break;
                default:
            }
        }
    }

    private getPrevious(node: TreeNode): TreeNode {
        if (node == null) {
            return this.categoriesTree[0];
        }
        const childrenList = node.parent == null ? this.categoriesTree : node.parent.children;
        const index = childrenList.indexOf(node);
        if (index > 0) {
            return childrenList[index - 1];
        } else {
            return node.parent;
        }
    }

    private getNext(node: TreeNode): TreeNode {
        if (node == null) {
            return this.categoriesTree[0];
        }
        const childrenList = node.parent == null ? this.categoriesTree : node.parent.children;
        const index = childrenList.indexOf(node);
        if (index < childrenList.length - 1) {
            return childrenList[index + 1];
        } else {
            return this.getNext(node.parent);
        }
    }

    private hide() {
        this.displayTree = false;
    }

    private show() {
        this.displayTree = true;
    }

    private createTreeNodeFromCategory(category: IMaterialCategory): TreeNode {
        return { label: category.name, data: category, selectable: true, children: [] };
    }

    search() {
        if (this.filterQuery != null && this.filterQuery.length > 2) {
            const searchTerm = this.filterQuery.toLocaleLowerCase();
            const rootTree: TreeNode[] = [];
            this.internalCategoriesTree.forEach(node => {
                this.expandRecursive(node, searchTerm, null, rootTree);
            });
            this.categoriesTree = rootTree;
        } else {
            this.categoriesTree = this.internalCategoriesTree;
        }
    }

    expandRecursive(node: TreeNode, query: string, nodeParent: TreeNode, rootTree: TreeNode[]): boolean {
        let result = false;
        const newNode: TreeNode = { label: node.label, data: node.data, selectable: node.selectable, children: [] };
        result = this.queryMatchTreeNode(query, node);
        if (node.children) {
            node.children.forEach(childNode => {
                result = this.expandRecursive(childNode, query, newNode, rootTree) || result;
            });
        }
        if (result) {
            newNode.expanded = true;
            if (nodeParent == null) {
                rootTree.push(newNode);
            } else {
                nodeParent.children.push(newNode);
            }
        }
        return result;
    }

    private queryMatchTreeNode(query: string, node: TreeNode): boolean {
        const result =
            node.data.name.toLocaleLowerCase().includes(query) || (node.data.description != null && node.data.description.includes(query));
        return result;
    }

    private buildCategoryTreeNode(categories: IMaterialCategory[]) {
        this.treeNodeList = categories.map(this.createTreeNodeFromCategory);
        this.internalCategoriesTree = [];
        this.treeNodeList.forEach(node => {
            if (node.data.parentMaterialCategory != null) {
                const nodeParent = this.treeNodeList.find(nodeToFind => nodeToFind.data.id === node.data.parentMaterialCategory.id);
                node.parent = nodeParent;
                // nodeParent.selectable = false;
                nodeParent.children.push(node);
            } else {
                this.internalCategoriesTree.push(node);
            }
        });
        console.log(this.treeNodeList);
        this.categoriesTree = this.internalCategoriesTree;
    }

    onFocus() {
        this.show();
    }

    onNodeSelect(event: any) {
        this.nodeSelect(event.node);
    }

    private nodeSelect(node: TreeNode) {
        this.value = node.data;
        this._filterQuery = this.value.name;
        this.hide();
    }
    /**
     * Invoked when the model has been changed
     */
    onChange: (_: any) => void = (_: any) => {};

    /**
     * Invoked when the model has been touched
     */
    onTouched: () => void = () => {};

    writeValue(value: any): void {
        if (value !== undefined && value != null) {
            this.value = value;
        } else {
            this.value = null;
        }
    }

    /**
     * Registers a callback function that should be called when the control's value changes in the UI.
     * @param fn
     */
    registerOnChange(fn: any): void {
        this.onChange = fn;
    }

    /**
     * Registers a callback function that should be called when the control receives a blur event.
     * @param fn
     */
    registerOnTouched(fn: any): void {
        this.onTouched = fn;
    }

    setDisabledState?(isDisabled: boolean): void {}

    public get value(): any {
        return this.selectedData;
    }

    // set accessor including call the onchange callback
    public set value(v: any) {
        if (v !== this.selectedData) {
            this.selectedData = v;
            this.onChange(v);
        }
    }
}
