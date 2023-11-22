import {APP_INITIALIZER, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http'
import {AgGridModule} from 'ag-grid-angular';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {RegionComponent} from './geocache-region/app.component';
import {GeocacheDetailComponent} from './geocache-detail/app.component';
import {CountyComponent} from './geocache-county/app.component';
import {AppConfigService} from "./app-config";

/**
 * Exported function so that it works with AOT
 * @param {AppConfigService} configService
 * @returns {Function}
 */
export function loadConfigService(configService: AppConfigService): Function {
  return () => {
    return configService.load()
  };
}

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
  providers: [
    AppConfigService,
    {provide: APP_INITIALIZER, useFactory: loadConfigService, deps: [AppConfigService], multi: true},
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
