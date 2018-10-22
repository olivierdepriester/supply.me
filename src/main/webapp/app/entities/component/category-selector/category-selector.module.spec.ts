import { CategorySelectorModule } from './category-selector.module';

describe('CategorySelectorModule', () => {
    let categorySelectorModule: CategorySelectorModule;

    beforeEach(() => {
        categorySelectorModule = new CategorySelectorModule();
    });

    it('should create an instance', () => {
        expect(categorySelectorModule).toBeTruthy();
    });
});
