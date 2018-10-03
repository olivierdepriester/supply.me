import { SupplierSelectorModule } from './supplier-selector.module';

describe('SupplierSelectorModule', () => {
    let supplierSelectorModule: SupplierSelectorModule;

    beforeEach(() => {
        supplierSelectorModule = new SupplierSelectorModule();
    });

    it('should create an instance', () => {
        expect(supplierSelectorModule).toBeTruthy();
    });
});
