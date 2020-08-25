import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { CsvserverTestModule } from '../../../test.module';
import { CsvfileUpdateComponent } from 'app/entities/csvfile/csvfile-update.component';
import { CsvfileService } from 'app/entities/csvfile/csvfile.service';
import { Csvfile } from 'app/shared/model/csvfile.model';

describe('Component Tests', () => {
  describe('Csvfile Management Update Component', () => {
    let comp: CsvfileUpdateComponent;
    let fixture: ComponentFixture<CsvfileUpdateComponent>;
    let service: CsvfileService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CsvserverTestModule],
        declarations: [CsvfileUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CsvfileUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CsvfileUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CsvfileService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Csvfile(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Csvfile();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
