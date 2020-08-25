import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'csvfile',
        loadChildren: () => import('./csvfile/csvfile.module').then(m => m.CsvserverCsvfileModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class CsvserverEntityModule {}
