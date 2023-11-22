import {HttpClient} from '@angular/common/http';
import {Component, EventEmitter, Input, Output, ViewChild} from '@angular/core';
import {AgGridAngular} from 'ag-grid-angular';
import {CellClickedEvent, ColDef, GridReadyEvent} from 'ag-grid-community';
import {Observable} from 'rxjs';
import {APP_CONFIG} from "../app-config";

@Component({
  selector: 'county',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class CountyComponent {

  @Output() messageToParent = new EventEmitter<string>();


 @Input() set messageToCounty(value: string) {
   this.http
     .get<any[]>('http://' + APP_CONFIG.host_url + '/uicache/counties/' + value).subscribe((data) => {
     this.countygrid.api.setRowData(data);
   });

 }

    // Each Column Definition results in one Column.
   public countyColumnDefs: ColDef[] =  [
      { headerName: 'County', field: 'county'},
      { headerName: 'Region', field: 'region'},
      { headerName: 'Count', field: 'count'},
    ];

  // DefaultColDef sets props common to all Columns
  public defaultColDef: ColDef = {
    sortable: true,
    filter: true,
  };

  // Data that gets displayed in the grid
  public rowData$!: Observable<any[]>;

  // For accessing the Grid's API
  @ViewChild(AgGridAngular)
  public countygrid!: AgGridAngular;

  constructor(private http: HttpClient) {}

  // Example load data from server
  onCountyGridReady(params: GridReadyEvent) {

  }


  onCountyCellClicked( e: CellClickedEvent): void {
    this.messageToParent.emit(e.data.county);
  }
}
