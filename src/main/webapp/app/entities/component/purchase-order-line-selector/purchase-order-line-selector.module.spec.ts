import { PurchaseOrderLineSelectorModule } from './purchase-order-line-selector.module';

describe('PurchaseOrderLineSelectorModule', () => {
    let purchaseOrderLineSelectorModule: PurchaseOrderLineSelectorModule;

    beforeEach(() => {
        purchaseOrderLineSelectorModule = new PurchaseOrderLineSelectorModule();
    });

    it('should create an instance', () => {
        expect(purchaseOrderLineSelectorModule).toBeTruthy();
    });
});
