import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MediafurySharedModule } from '../shared';

import { ABOUT_ROUTE, AboutComponent } from './';

@NgModule({
    imports: [
        MediafurySharedModule,
        RouterModule.forChild([ ABOUT_ROUTE ])
    ],
    declarations: [
        AboutComponent,
    ],
    entryComponents: [
    ],
    providers: [
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MediafuryAboutModule {}
