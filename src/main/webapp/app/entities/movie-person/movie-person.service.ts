import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { MoviePerson } from './movie-person.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<MoviePerson>;

@Injectable()
export class MoviePersonService {

    private resourceUrl =  SERVER_API_URL + 'api/movie-people';

    constructor(private http: HttpClient) { }

    create(moviePerson: MoviePerson): Observable<EntityResponseType> {
        const copy = this.convert(moviePerson);
        return this.http.post<MoviePerson>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(moviePerson: MoviePerson): Observable<EntityResponseType> {
        const copy = this.convert(moviePerson);
        return this.http.put<MoviePerson>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<MoviePerson>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<MoviePerson[]>> {
        const options = createRequestOption(req);
        return this.http.get<MoviePerson[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<MoviePerson[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: MoviePerson = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<MoviePerson[]>): HttpResponse<MoviePerson[]> {
        const jsonResponse: MoviePerson[] = res.body;
        const body: MoviePerson[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to MoviePerson.
     */
    private convertItemFromServer(moviePerson: MoviePerson): MoviePerson {
        const copy: MoviePerson = Object.assign({}, moviePerson);
        return copy;
    }

    /**
     * Convert a MoviePerson to a JSON which can be sent to the server.
     */
    private convert(moviePerson: MoviePerson): MoviePerson {
        const copy: MoviePerson = Object.assign({}, moviePerson);
        return copy;
    }
}
