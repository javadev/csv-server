import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { CsvserverSharedModule } from 'app/shared/shared.module';
import { CsvserverCoreModule } from 'app/core/core.module';
import { CsvserverAppRoutingModule } from './app-routing.module';
import { CsvserverHomeModule } from './home/home.module';
import { CsvserverEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    CsvserverSharedModule,
    CsvserverCoreModule,
    CsvserverHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    CsvserverEntityModule,
    CsvserverAppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent]
})
export class CsvserverAppModule {}
