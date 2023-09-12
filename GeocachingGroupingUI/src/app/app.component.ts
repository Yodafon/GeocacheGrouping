import { HttpClient } from '@angular/common/http';
import { Component, ViewChild } from '@angular/core';
import { AgGridAngular } from 'ag-grid-angular';
import { CellClickedEvent, ColDef, GridReadyEvent } from 'ag-grid-community';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  constructor() {}

  public messageToGeocacheDetail!: string;
  public messageToCounty!: string;

  // Cross component data passing for sibling componets

    getDataFromRegion(message: any) {
      this.messageToCounty = message;
    }


    getDataFromCounty(message: any) {
          this.messageToGeocacheDetail = message;

    }

}
