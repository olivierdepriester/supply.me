import { Injectable } from '@angular/core';
import { SORTED_AUTHORITIES } from 'app/app.constants';
import { Observable, Subject } from 'rxjs';
import { AccountService } from './account.service';

@Injectable({ providedIn: 'root' })
export class Principal {
    private userIdentity: any;
    private authenticated = false;
    private authenticationState = new Subject<any>();
    private highestValidationAuthorityLevel: string;

    constructor(private account: AccountService) {}

    authenticate(identity) {
        this.userIdentity = identity;
        this.authenticated = identity !== null;
        this.authenticationState.next(this.userIdentity);
    }

    hasAnyAuthority(authorities: string[]): Promise<boolean> {
        return Promise.resolve(this.hasAnyAuthorityDirect(authorities));
    }

    hasAnyAuthorityDirect(authorities: string[]): boolean {
        if (!this.authenticated || !this.userIdentity || !this.userIdentity.authorities) {
            return false;
        }

        for (let i = 0; i < authorities.length; i++) {
            if (this.userIdentity.authorities.includes(authorities[i])) {
                return true;
            }
        }

        return false;
    }

    hasAuthority(authority: string): Promise<boolean> {
        if (!this.authenticated) {
            return Promise.resolve(false);
        }

        return this.identity().then(
            id => {
                return Promise.resolve(id.authorities && id.authorities.includes(authority));
            },
            () => {
                return Promise.resolve(false);
            }
        );
    }

    identity(force?: boolean): Promise<any> {
        if (force === true) {
            this.userIdentity = undefined;
        }

        // check and see if we have retrieved the userIdentity data from the server.
        // if we have, reuse it by immediately resolving
        if (this.userIdentity) {
            return Promise.resolve(this.userIdentity);
        }

        // retrieve the userIdentity data from the server, update the identity object, and then resolve.
        return this.account
            .get()
            .toPromise()
            .then(response => {
                const account = response.body;
                if (account) {
                    this.userIdentity = account;
                    this.authenticated = true;
                    this.highestValidationAuthorityLevel = this.getHighestValidationAuthority();
                    console.log(`auth : ${this.userIdentity.authorities}`);
                    console.log(`ici : ${this.getHighestValidationAuthority()}`);
                } else {
                    this.userIdentity = null;
                    this.authenticated = false;
                }
                this.authenticationState.next(this.userIdentity);
                return this.userIdentity;
            })
            .catch(err => {
                this.userIdentity = null;
                this.authenticated = false;
                this.authenticationState.next(this.userIdentity);
                return null;
            });
    }

    private getHighestValidationAuthority(): string {
        return this.userIdentity.authorities
            .filter(a => SORTED_AUTHORITIES.indexOf(a) >= 0)
            .reduce((prev, next) => this.getHigherAuthority(prev, next), null);
    }

    getCurrentUserHighestValidationAuthority(): string {
        return this.highestValidationAuthorityLevel;
    }

    getHigherAuthority(x: string, y: string): string {
        return this.compare(x, y) > 0 ? x : y;
    }

    compare(x: string, y: string): number {
        if (x == null || x === undefined || x === '') {
            return -1;
        } else if (y == null || y === undefined || y === '') {
            return 1;
        } else {
            return Math.sign(SORTED_AUTHORITIES.indexOf(x) - SORTED_AUTHORITIES.indexOf(y));
        }
    }

    isAuthenticated(): boolean {
        return this.authenticated;
    }

    isIdentityResolved(): boolean {
        return this.userIdentity !== undefined;
    }

    getAuthenticationState(): Observable<any> {
        return this.authenticationState.asObservable();
    }

    getImageUrl(): string {
        return this.isIdentityResolved() ? this.userIdentity.imageUrl : null;
    }
}
