import { BaseEntity } from './../../shared';

export const enum PersonRole {
    'WRITER',
    'ACTOR',
    'DIRECTOR'
}

export class MoviePerson implements BaseEntity {
    constructor(
        public id?: number,
        public role?: PersonRole,
        public movieId?: number,
        public personId?: number,
    ) {
    }
}
