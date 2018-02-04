import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MediafurySharedModule } from '../../shared';
import {
    VoteService,
    VotePopupService,
    VoteComponent,
    VoteDetailComponent,
    VoteDialogComponent,
    VotePopupComponent,
    VoteDeletePopupComponent,
    VoteDeleteDialogComponent,
    voteRoute,
    votePopupRoute,
    VoteResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...voteRoute,
    ...votePopupRoute,
];

@NgModule({
    imports: [
        MediafurySharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        VoteComponent,
        VoteDetailComponent,
        VoteDialogComponent,
        VoteDeleteDialogComponent,
        VotePopupComponent,
        VoteDeletePopupComponent,
    ],
    entryComponents: [
        VoteComponent,
        VoteDialogComponent,
        VotePopupComponent,
        VoteDeleteDialogComponent,
        VoteDeletePopupComponent,
    ],
    providers: [
        VoteService,
        VotePopupService,
        VoteResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MediafuryVoteModule {}
