/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { MediafuryTestModule } from '../../../test.module';
import { MovieRandomComponent } from '../../../../../../main/webapp/app/entities/movie/movie-random/movie-random.component';
import { MovieService } from '../../../../../../main/webapp/app/entities/movie/movie.service';
import { Movie } from '../../../../../../main/webapp/app/entities/movie/movie.model';

describe('Component Tests', () => {

    describe('Movie Management Random Component', () => {
        let comp: MovieRandomComponent;
        let fixture: ComponentFixture<MovieRandomComponent>;
        let service: MovieService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [MediafuryTestModule],
                declarations: [MovieRandomComponent],
                providers: [
                    MovieService
                ]
            })
            .overrideTemplate(MovieRandomComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MovieRandomComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MovieService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'random').and.returnValue(Observable.of(new HttpResponse({
                    body: new Movie(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.random).toHaveBeenCalled();
                expect(comp.movie).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
