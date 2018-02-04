import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Vote } from './vote.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Vote>;

@Injectable()
export class VoteService {

    private resourceUrl =  SERVER_API_URL + 'api/votes';

    constructor(private http: HttpClient) { }

    create(vote: Vote): Observable<EntityResponseType> {
        const copy = this.convert(vote);
        return this.http.post<Vote>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(vote: Vote): Observable<EntityResponseType> {
        const copy = this.convert(vote);
        return this.http.put<Vote>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Vote>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Vote[]>> {
        const options = createRequestOption(req);
        return this.http.get<Vote[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Vote[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Vote = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Vote[]>): HttpResponse<Vote[]> {
        const jsonResponse: Vote[] = res.body;
        const body: Vote[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Vote.
     */
    private convertItemFromServer(vote: Vote): Vote {
        const copy: Vote = Object.assign({}, vote);
        return copy;
    }

    /**
     * Convert a Vote to a JSON which can be sent to the server.
     */
    private convert(vote: Vote): Vote {
        const copy: Vote = Object.assign({}, vote);
        return copy;
    }
}
