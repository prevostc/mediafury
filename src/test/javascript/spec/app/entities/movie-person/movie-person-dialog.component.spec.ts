/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { MediafuryTestModule } from '../../../test.module';
import { MoviePersonDialogComponent } from '../../../../../../main/webapp/app/entities/movie-person/movie-person-dialog.component';
import { MoviePersonService } from '../../../../../../main/webapp/app/entities/movie-person/movie-person.service';
import { MoviePerson } from '../../../../../../main/webapp/app/entities/movie-person/movie-person.model';
import { MovieService } from '../../../../../../main/webapp/app/entities/movie';
import { PersonService } from '../../../../../../main/webapp/app/entities/person';

describe('Component Tests', () => {

    describe('MoviePerson Management Dialog Component', () => {
        let comp: MoviePersonDialogComponent;
        let fixture: ComponentFixture<MoviePersonDialogComponent>;
        let service: MoviePersonService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [MediafuryTestModule],
                declarations: [MoviePersonDialogComponent],
                providers: [
                    MovieService,
                    PersonService,
                    MoviePersonService
                ]
            })
            .overrideTemplate(MoviePersonDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MoviePersonDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MoviePersonService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new MoviePerson(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.moviePerson = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'moviePersonListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new MoviePerson();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.moviePerson = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'moviePersonListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
