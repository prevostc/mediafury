/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { MediafuryTestModule } from '../../../test.module';
import { MoviePersonComponent } from '../../../../../../main/webapp/app/entities/movie-person/movie-person.component';
import { MoviePersonService } from '../../../../../../main/webapp/app/entities/movie-person/movie-person.service';
import { MoviePerson } from '../../../../../../main/webapp/app/entities/movie-person/movie-person.model';

describe('Component Tests', () => {

    describe('MoviePerson Management Component', () => {
        let comp: MoviePersonComponent;
        let fixture: ComponentFixture<MoviePersonComponent>;
        let service: MoviePersonService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [MediafuryTestModule],
                declarations: [MoviePersonComponent],
                providers: [
                    MoviePersonService
                ]
            })
            .overrideTemplate(MoviePersonComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MoviePersonComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MoviePersonService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new MoviePerson(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.moviePeople[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
