import { HttpClient } from '@angular/common/http';
import { Component, ViewChild } from '@angular/core';
import { AgGridAngular } from 'ag-grid-angular';
import { CellClickedEvent, ColDef, GridReadyEvent } from 'ag-grid-community';
import { Observable } from 'rxjs';

@Component({
  selector: 'countygridcomponent',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class CountyComponent {

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

    // Example of consuming Grid Event
    public loadCounties(e: CellClickedEvent): void {
             this.http
            .get<any[]>('http://localhost:8081/uicache/counties/' + e.data.region).subscribe((data)=>{
             this.countygrid.api.setRowData(data);
            });
    }


  // Example using Grid's API
  clearSelection(): void {
    this.countygrid.api.deselectAll();
  }
}
