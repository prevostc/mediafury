import { BaseEntity } from './../../shared';

export class Person implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public moviePeople?: BaseEntity[],
    ) {
    }
}
