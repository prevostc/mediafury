import { BaseEntity } from './../../shared';

export class Vote implements BaseEntity {
    constructor(
        public id?: number,
        public winnerEloDiff?: number,
        public loserEloDiff?: number,
        public winnerId?: number,
        public loserId?: number,
    ) {
    }
}
