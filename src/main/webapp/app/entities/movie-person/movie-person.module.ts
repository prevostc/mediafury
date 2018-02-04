import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MediafurySharedModule } from '../../shared';
import {
    MoviePersonService,
    MoviePersonPopupService,
    MoviePersonComponent,
    MoviePersonDetailComponent,
    MoviePersonDialogComponent,
    MoviePersonPopupComponent,
    MoviePersonDeletePopupComponent,
    MoviePersonDeleteDialogComponent,
    moviePersonRoute,
    moviePersonPopupRoute,
    MoviePersonResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...moviePersonRoute,
    ...moviePersonPopupRoute,
];

@NgModule({
    imports: [
        MediafurySharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        MoviePersonComponent,
        MoviePersonDetailComponent,
        MoviePersonDialogComponent,
        MoviePersonDeleteDialogComponent,
        MoviePersonPopupComponent,
        MoviePersonDeletePopupComponent,
    ],
    entryComponents: [
        MoviePersonComponent,
        MoviePersonDialogComponent,
        MoviePersonPopupComponent,
        MoviePersonDeleteDialogComponent,
        MoviePersonDeletePopupComponent,
    ],
    providers: [
        MoviePersonService,
        MoviePersonPopupService,
        MoviePersonResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MediafuryMoviePersonModule {}
