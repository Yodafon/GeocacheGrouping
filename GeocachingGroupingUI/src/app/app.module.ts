import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http'
import { AgGridModule } from 'ag-grid-angular';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RegionComponent } from './geocache-region/app.component';
import { GeocacheDetailComponent } from './geocache-detail/app.component';
import { CountyComponent } from './geocache-county/app.component';

@NgModule({
  declarations: [
    AppComponent,
    GeocacheDetailComponent,
    RegionComponent,
    CountyComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AgGridModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
