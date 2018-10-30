import { DemandCategorySelectorModule } from './demand-category-selector.module';

describe('DemandCategorySelectorModule', () => {
    let demandCategorySelectorModule: DemandCategorySelectorModule;

    beforeEach(() => {
        demandCategorySelectorModule = new DemandCategorySelectorModule();
    });

    it('should create an instance', () => {
        expect(demandCategorySelectorModule).toBeTruthy();
    });
});
