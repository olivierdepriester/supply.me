import { UserSelectorModule } from './user-selector.module';

describe('UserSelectorModule', () => {
    let userSelectorModule: UserSelectorModule;

    beforeEach(() => {
        userSelectorModule = new UserSelectorModule();
    });

    it('should create an instance', () => {
        expect(userSelectorModule).toBeTruthy();
    });
});
