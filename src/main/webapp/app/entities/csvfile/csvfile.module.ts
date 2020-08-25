import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CsvserverSharedModule } from 'app/shared/shared.module';
import { CsvfileComponent } from './csvfile.component';
import { CsvfileDetailComponent } from './csvfile-detail.component';
import { CsvfileUpdateComponent } from './csvfile-update.component';
import { CsvfileDeleteDialogComponent } from './csvfile-delete-dialog.component';
import { csvfileRoute } from './csvfile.route';

@NgModule({
  imports: [CsvserverSharedModule, RouterModule.forChild(csvfileRoute)],
  declarations: [CsvfileComponent, CsvfileDetailComponent, CsvfileUpdateComponent, CsvfileDeleteDialogComponent],
  entryComponents: [CsvfileDeleteDialogComponent]
})
export class CsvserverCsvfileModule {}
