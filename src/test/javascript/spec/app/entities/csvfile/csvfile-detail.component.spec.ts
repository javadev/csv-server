import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { CsvserverTestModule } from '../../../test.module';
import { CsvfileDetailComponent } from 'app/entities/csvfile/csvfile-detail.component';
import { Csvfile } from 'app/shared/model/csvfile.model';

describe('Component Tests', () => {
  describe('Csvfile Management Detail Component', () => {
    let comp: CsvfileDetailComponent;
    let fixture: ComponentFixture<CsvfileDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ csvfile: new Csvfile(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CsvserverTestModule],
        declarations: [CsvfileDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CsvfileDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CsvfileDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load csvfile on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.csvfile).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeContentType, fakeBase64);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeContentType, fakeBase64);
      });
    });
  });
});
