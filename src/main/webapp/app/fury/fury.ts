import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MediafurySharedModule } from '../shared';
import { MediafuryMovieModule } from '../entities/movie/movie.module';

import { FURY_ROUTE, FuryComponent } from './';

@NgModule({
    imports: [
        MediafurySharedModule,
        MediafuryMovieModule,
        RouterModule.forChild([ FURY_ROUTE ])
    ],
    declarations: [
        FuryComponent,
    ],
    entryComponents: [
    ],
    providers: [
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MediafuryFuryModule {}
