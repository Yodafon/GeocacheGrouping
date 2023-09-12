import { HttpClient } from '@angular/common/http';
import { Component, ViewChild, Input, Output, EventEmitter } from '@angular/core';
import { AgGridAngular } from 'ag-grid-angular';
import { CellClickedEvent, ColDef, GridReadyEvent } from 'ag-grid-community';
import { Observable } from 'rxjs';
import { CountyComponent } from '../geocache-county/app.component';

@Component({
  selector: 'region',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class RegionComponent {

  public regionColumnDefs: ColDef[] = [
    { headerName: 'Region', field: 'region'},
    { headerName: 'Count', field: 'count'}
  ];

  // DefaultColDef sets props common to all Columns
  public defaultColDef: ColDef = {
    sortable: true,
    filter: true,
  };

  // Data that gets displayed in the grid
  public rowData$!: Observable<any[]>;

  // For accessing the Grid's API
  @ViewChild(AgGridAngular) agGrid!: AgGridAngular;

  @Output() messageToParent = new EventEmitter<string>();

  constructor(private http: HttpClient) {}

  // Example load data from server
  onRegionGridReady(params: GridReadyEvent) {
     this.rowData$ = this.http
       .get<any[]>('http://localhost:8081/uicache/regions');
  }

  // Example of consuming Grid Event
  onRegionCellClicked( e: CellClickedEvent): void {
    console.log('cellClicked', e.data.region);
    this.messageToParent.emit(e.data.region);
  }



  // Example using Grid's API
  clearSelection(): void {
    this.agGrid.api.deselectAll();
  }
}
