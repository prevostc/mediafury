import { BaseEntity } from './../../shared';

export class Movie implements BaseEntity {
    constructor(
        public id?: number,
        public title?: string,
        public year?: number,
        public plot?: string,
        public imageUrl?: string,
        public elo?: number,
        public moviePeople?: BaseEntity[],
        public categories?: BaseEntity[],
    ) {
    }
}
