import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MediafurySharedModule } from '../../shared';
import {
    PersonService,
    PersonPopupService,
    PersonComponent,
    PersonDetailComponent,
    PersonDialogComponent,
    PersonPopupComponent,
    PersonDeletePopupComponent,
    PersonDeleteDialogComponent,
    personRoute,
    personPopupRoute,
    PersonResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...personRoute,
    ...personPopupRoute,
];

@NgModule({
    imports: [
        MediafurySharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        PersonComponent,
        PersonDetailComponent,
        PersonDialogComponent,
        PersonDeleteDialogComponent,
        PersonPopupComponent,
        PersonDeletePopupComponent,
    ],
    entryComponents: [
        PersonComponent,
        PersonDialogComponent,
        PersonPopupComponent,
        PersonDeleteDialogComponent,
        PersonDeletePopupComponent,
    ],
    providers: [
        PersonService,
        PersonPopupService,
        PersonResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MediafuryPersonModule {}
