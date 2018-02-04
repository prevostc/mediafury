import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { MediafuryMovieModule } from './movie/movie.module';
import { MediafuryCategoryModule } from './category/category.module';
import { MediafuryPersonModule } from './person/person.module';
import { MediafuryMoviePersonModule } from './movie-person/movie-person.module';
import { MediafuryVoteModule } from './vote/vote.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        MediafuryMovieModule,
        MediafuryCategoryModule,
        MediafuryPersonModule,
        MediafuryMoviePersonModule,
        MediafuryVoteModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MediafuryEntityModule {}
