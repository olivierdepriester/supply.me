import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SupplyMeButtonDemoModule } from './buttons/button/buttondemo.module';
import { SupplyMeSplitbuttonDemoModule } from './buttons/splitbutton/splitbuttondemo.module';

import { SupplyMeDialogDemoModule } from './overlay/dialog/dialogdemo.module';
import { SupplyMeConfirmDialogDemoModule } from './overlay/confirmdialog/confirmdialogdemo.module';
import { SupplyMeLightboxDemoModule } from './overlay/lightbox/lightboxdemo.module';
import { SupplyMeTooltipDemoModule } from './overlay/tooltip/tooltipdemo.module';
import { SupplyMeOverlayPanelDemoModule } from './overlay/overlaypanel/overlaypaneldemo.module';
import { SupplyMeSideBarDemoModule } from './overlay/sidebar/sidebardemo.module';

import { SupplyMeKeyFilterDemoModule } from './inputs/keyfilter/keyfilterdemo.module';
import { SupplyMeInputTextDemoModule } from './inputs/inputtext/inputtextdemo.module';
import { SupplyMeInputTextAreaDemoModule } from './inputs/inputtextarea/inputtextareademo.module';
import { SupplyMeInputGroupDemoModule } from './inputs/inputgroup/inputgroupdemo.module';
import { SupplyMeCalendarDemoModule } from './inputs/calendar/calendardemo.module';
import { SupplyMeCheckboxDemoModule } from './inputs/checkbox/checkboxdemo.module';
import { SupplyMeChipsDemoModule } from './inputs/chips/chipsdemo.module';
import { SupplyMeColorPickerDemoModule } from './inputs/colorpicker/colorpickerdemo.module';
import { SupplyMeInputMaskDemoModule } from './inputs/inputmask/inputmaskdemo.module';
import { SupplyMeInputSwitchDemoModule } from './inputs/inputswitch/inputswitchdemo.module';
import { SupplyMePasswordIndicatorDemoModule } from './inputs/passwordindicator/passwordindicatordemo.module';
import { SupplyMeAutoCompleteDemoModule } from './inputs/autocomplete/autocompletedemo.module';
import { SupplyMeSliderDemoModule } from './inputs/slider/sliderdemo.module';
import { SupplyMeSpinnerDemoModule } from './inputs/spinner/spinnerdemo.module';
import { SupplyMeRatingDemoModule } from './inputs/rating/ratingdemo.module';
import { SupplyMeSelectDemoModule } from './inputs/select/selectdemo.module';
import { SupplyMeSelectButtonDemoModule } from './inputs/selectbutton/selectbuttondemo.module';
import { SupplyMeListboxDemoModule } from './inputs/listbox/listboxdemo.module';
import { SupplyMeRadioButtonDemoModule } from './inputs/radiobutton/radiobuttondemo.module';
import { SupplyMeToggleButtonDemoModule } from './inputs/togglebutton/togglebuttondemo.module';
import { SupplyMeEditorDemoModule } from './inputs/editor/editordemo.module';

import { SupplyMeGrowlDemoModule } from './messages/growl/growldemo.module';
import { SupplyMeMessagesDemoModule } from './messages/messages/messagesdemo.module';
/*import { SupplyMeToastDemoModule } from './messages/toast/toastdemo.module';*/
import { SupplyMeGalleriaDemoModule } from './multimedia/galleria/galleriademo.module';

import { SupplyMeFileUploadDemoModule } from './fileupload/fileupload/fileuploaddemo.module';

import { SupplyMeAccordionDemoModule } from './panel/accordion/accordiondemo.module';
import { SupplyMePanelDemoModule } from './panel/panel/paneldemo.module';
import { SupplyMeTabViewDemoModule } from './panel/tabview/tabviewdemo.module';
import { SupplyMeFieldsetDemoModule } from './panel/fieldset/fieldsetdemo.module';
import { SupplyMeToolbarDemoModule } from './panel/toolbar/toolbardemo.module';
import { SupplyMeGridDemoModule } from './panel/grid/griddemo.module';
import { SupplyMeScrollPanelDemoModule } from './panel/scrollpanel/scrollpaneldemo.module';
import { SupplyMeCardDemoModule } from './panel/card/carddemo.module';

import { SupplyMeDataTableDemoModule } from './data/datatable/datatabledemo.module';
import { SupplyMeTableDemoModule } from './data/table/tabledemo.module';
import { SupplyMeDataGridDemoModule } from './data/datagrid/datagriddemo.module';
import { SupplyMeDataListDemoModule } from './data/datalist/datalistdemo.module';
import { SupplyMeDataScrollerDemoModule } from './data/datascroller/datascrollerdemo.module';
import { SupplyMePickListDemoModule } from './data/picklist/picklistdemo.module';
import { SupplyMeOrderListDemoModule } from './data/orderlist/orderlistdemo.module';
import { SupplyMeScheduleDemoModule } from './data/schedule/scheduledemo.module';
import { SupplyMeTreeDemoModule } from './data/tree/treedemo.module';
import { SupplyMeTreeTableDemoModule } from './data/treetable/treetabledemo.module';
import { SupplyMePaginatorDemoModule } from './data/paginator/paginatordemo.module';
import { SupplyMeGmapDemoModule } from './data/gmap/gmapdemo.module';
import { SupplyMeOrgChartDemoModule } from './data/orgchart/orgchartdemo.module';
import { SupplyMeCarouselDemoModule } from './data/carousel/carouseldemo.module';
import { SupplyMeDataViewDemoModule } from './data/dataview/dataviewdemo.module';

import { SupplyMeBarchartDemoModule } from './charts/barchart/barchartdemo.module';
import { SupplyMeDoughnutchartDemoModule } from './charts/doughnutchart/doughnutchartdemo.module';
import { SupplyMeLinechartDemoModule } from './charts/linechart/linechartdemo.module';
import { SupplyMePiechartDemoModule } from './charts/piechart/piechartdemo.module';
import { SupplyMePolarareachartDemoModule } from './charts/polarareachart/polarareachartdemo.module';
import { SupplyMeRadarchartDemoModule } from './charts/radarchart/radarchartdemo.module';

import { SupplyMeDragDropDemoModule } from './dragdrop/dragdrop/dragdropdemo.module';

import { SupplyMeMenuDemoModule } from './menu/menu/menudemo.module';
import { SupplyMeContextMenuDemoModule } from './menu/contextmenu/contextmenudemo.module';
import { SupplyMePanelMenuDemoModule } from './menu/panelmenu/panelmenudemo.module';
import { SupplyMeStepsDemoModule } from './menu/steps/stepsdemo.module';
import { SupplyMeTieredMenuDemoModule } from './menu/tieredmenu/tieredmenudemo.module';
import { SupplyMeBreadcrumbDemoModule } from './menu/breadcrumb/breadcrumbdemo.module';
import { SupplyMeMegaMenuDemoModule } from './menu/megamenu/megamenudemo.module';
import { SupplyMeMenuBarDemoModule } from './menu/menubar/menubardemo.module';
import { SupplyMeSlideMenuDemoModule } from './menu/slidemenu/slidemenudemo.module';
import { SupplyMeTabMenuDemoModule } from './menu/tabmenu/tabmenudemo.module';

import { SupplyMeBlockUIDemoModule } from './misc/blockui/blockuidemo.module';
import { SupplyMeCaptchaDemoModule } from './misc/captcha/captchademo.module';
import { SupplyMeDeferDemoModule } from './misc/defer/deferdemo.module';
import { SupplyMeInplaceDemoModule } from './misc/inplace/inplacedemo.module';
import { SupplyMeProgressBarDemoModule } from './misc/progressbar/progressbardemo.module';
import { SupplyMeRTLDemoModule } from './misc/rtl/rtldemo.module';
import { SupplyMeTerminalDemoModule } from './misc/terminal/terminaldemo.module';
import { SupplyMeValidationDemoModule } from './misc/validation/validationdemo.module';
import { SupplyMeProgressSpinnerDemoModule } from './misc/progressspinner/progressspinnerdemo.module';

@NgModule({
    imports: [
        SupplyMeMenuDemoModule,
        SupplyMeContextMenuDemoModule,
        SupplyMePanelMenuDemoModule,
        SupplyMeStepsDemoModule,
        SupplyMeTieredMenuDemoModule,
        SupplyMeBreadcrumbDemoModule,
        SupplyMeMegaMenuDemoModule,
        SupplyMeMenuBarDemoModule,
        SupplyMeSlideMenuDemoModule,
        SupplyMeTabMenuDemoModule,

        SupplyMeBlockUIDemoModule,
        SupplyMeCaptchaDemoModule,
        SupplyMeDeferDemoModule,
        SupplyMeInplaceDemoModule,
        SupplyMeProgressBarDemoModule,
        SupplyMeInputMaskDemoModule,
        SupplyMeRTLDemoModule,
        SupplyMeTerminalDemoModule,
        SupplyMeValidationDemoModule,

        SupplyMeButtonDemoModule,
        SupplyMeSplitbuttonDemoModule,

        SupplyMeInputTextDemoModule,
        SupplyMeInputTextAreaDemoModule,
        SupplyMeInputGroupDemoModule,
        SupplyMeCalendarDemoModule,
        SupplyMeChipsDemoModule,
        SupplyMeInputMaskDemoModule,
        SupplyMeInputSwitchDemoModule,
        SupplyMePasswordIndicatorDemoModule,
        SupplyMeAutoCompleteDemoModule,
        SupplyMeSliderDemoModule,
        SupplyMeSpinnerDemoModule,
        SupplyMeRatingDemoModule,
        SupplyMeSelectDemoModule,
        SupplyMeSelectButtonDemoModule,
        SupplyMeListboxDemoModule,
        SupplyMeRadioButtonDemoModule,
        SupplyMeToggleButtonDemoModule,
        SupplyMeEditorDemoModule,
        SupplyMeColorPickerDemoModule,
        SupplyMeCheckboxDemoModule,
        SupplyMeKeyFilterDemoModule,

        SupplyMeGrowlDemoModule,
        SupplyMeMessagesDemoModule,
        /*SupplyMeToastDemoModule,*/
        SupplyMeGalleriaDemoModule,

        SupplyMeFileUploadDemoModule,

        SupplyMeAccordionDemoModule,
        SupplyMePanelDemoModule,
        SupplyMeTabViewDemoModule,
        SupplyMeFieldsetDemoModule,
        SupplyMeToolbarDemoModule,
        SupplyMeGridDemoModule,
        SupplyMeScrollPanelDemoModule,
        SupplyMeCardDemoModule,

        SupplyMeBarchartDemoModule,
        SupplyMeDoughnutchartDemoModule,
        SupplyMeLinechartDemoModule,
        SupplyMePiechartDemoModule,
        SupplyMePolarareachartDemoModule,
        SupplyMeRadarchartDemoModule,

        SupplyMeDragDropDemoModule,

        SupplyMeDialogDemoModule,
        SupplyMeConfirmDialogDemoModule,
        SupplyMeLightboxDemoModule,
        SupplyMeTooltipDemoModule,
        SupplyMeOverlayPanelDemoModule,
        SupplyMeSideBarDemoModule,

        SupplyMeDataTableDemoModule,
        SupplyMeTableDemoModule,
        SupplyMeDataGridDemoModule,
        SupplyMeDataListDemoModule,
        SupplyMeDataViewDemoModule,
        SupplyMeDataScrollerDemoModule,
        SupplyMeScheduleDemoModule,
        SupplyMeOrderListDemoModule,
        SupplyMePickListDemoModule,
        SupplyMeTreeDemoModule,
        SupplyMeTreeTableDemoModule,
        SupplyMePaginatorDemoModule,
        SupplyMeOrgChartDemoModule,
        SupplyMeGmapDemoModule,
        SupplyMeCarouselDemoModule,
        SupplyMeProgressSpinnerDemoModule
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SupplyMeprimengModule {}
