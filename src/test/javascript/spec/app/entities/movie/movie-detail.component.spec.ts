/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { MediafuryTestModule } from '../../../test.module';
import { MovieDetailComponent } from '../../../../../../main/webapp/app/entities/movie/movie-detail.component';
import { MovieService } from '../../../../../../main/webapp/app/entities/movie/movie.service';
import { Movie } from '../../../../../../main/webapp/app/entities/movie/movie.model';

describe('Component Tests', () => {

    describe('Movie Management Detail Component', () => {
        let comp: MovieDetailComponent;
        let fixture: ComponentFixture<MovieDetailComponent>;
        let service: MovieService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [MediafuryTestModule],
                declarations: [MovieDetailComponent],
                providers: [
                    MovieService
                ]
            })
            .overrideTemplate(MovieDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MovieDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MovieService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Movie(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.movie).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
