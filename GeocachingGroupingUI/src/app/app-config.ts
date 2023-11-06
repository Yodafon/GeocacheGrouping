import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {throwError} from "rxjs";
import {catchError} from "rxjs/operators";

/**
 * Declaration of config class
 */
export class AppConfig {
//Your properties here
  readonly host_url: string = '';
}

/**
 * Global variable containing actual config to use. Initialised via ajax call
 */
export let APP_CONFIG: AppConfig;

/**
 * Service in charge of dynamically initialising configuration
 */
@Injectable()
export class AppConfigService {

  constructor(private http: HttpClient) {
  }

  public load() {
    return new Promise((resolve, reject) => {

      this.http.get('/assets/config/conf.json').subscribe((envResponse: any) => {
        let t = new AppConfig();
        //Modify envResponse here if needed (e.g. to ajust parameters for https,...)
        APP_CONFIG = Object.assign(t, envResponse);
        resolve(true);
        catchError((error: any): any => {
          reject(true);
          return throwError('Server error');
        })
      });

    });
  }
}
