import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICsvfile } from 'app/shared/model/csvfile.model';
import { CsvfileService } from './csvfile.service';

@Component({
  templateUrl: './csvfile-delete-dialog.component.html'
})
export class CsvfileDeleteDialogComponent {
  csvfile?: ICsvfile;

  constructor(protected csvfileService: CsvfileService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.csvfileService.delete(id).subscribe(() => {
      this.eventManager.broadcast('csvfileListModification');
      this.activeModal.close();
    });
  }
}
