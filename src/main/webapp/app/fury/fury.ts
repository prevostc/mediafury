import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MediafurySharedModule } from '../shared';
import { MediafuryMovieModule } from '../entities/movie/movie.module';

import { FURY_ROUTE, FuryComponent } from './';
import { FuryContestComponent } from './fury-contest/fury-contest.component';

@NgModule({
    imports: [
        MediafurySharedModule,
        MediafuryMovieModule,
        RouterModule.forChild([ FURY_ROUTE ])
    ],
    declarations: [
        FuryComponent,
        FuryContestComponent,
    ],
    entryComponents: [
    ],
    providers: [
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MediafuryFuryModule {}
