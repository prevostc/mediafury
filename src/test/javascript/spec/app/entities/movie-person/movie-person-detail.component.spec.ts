/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { MediafuryTestModule } from '../../../test.module';
import { MoviePersonDetailComponent } from '../../../../../../main/webapp/app/entities/movie-person/movie-person-detail.component';
import { MoviePersonService } from '../../../../../../main/webapp/app/entities/movie-person/movie-person.service';
import { MoviePerson } from '../../../../../../main/webapp/app/entities/movie-person/movie-person.model';

describe('Component Tests', () => {

    describe('MoviePerson Management Detail Component', () => {
        let comp: MoviePersonDetailComponent;
        let fixture: ComponentFixture<MoviePersonDetailComponent>;
        let service: MoviePersonService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [MediafuryTestModule],
                declarations: [MoviePersonDetailComponent],
                providers: [
                    MoviePersonService
                ]
            })
            .overrideTemplate(MoviePersonDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MoviePersonDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MoviePersonService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new MoviePerson(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.moviePerson).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
