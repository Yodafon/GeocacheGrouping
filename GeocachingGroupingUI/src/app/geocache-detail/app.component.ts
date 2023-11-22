import {HttpClient} from '@angular/common/http';
import {Component, Input, ViewChild} from '@angular/core';
import {AgGridAngular} from 'ag-grid-angular';
import {ColDef, GridReadyEvent} from 'ag-grid-community';
import {Observable} from 'rxjs';
import {APP_CONFIG} from "../app-config";

@Component({
  selector: 'geocachedetail',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class GeocacheDetailComponent {

  public geocachingDetailColumnDefs: ColDef[] = [
    {headerName: 'Id', field: 'id'},
        { headerName: 'Name', field: 'name'},
        { headerName: 'County', field: 'county'},
        { headerName: 'Region', field: 'region'},
        { headerName: 'Type', field: 'type'},
        { headerName: 'Lon.', field: 'lon'},
        { headerName: 'Lat.', field: 'lat'},
        { headerName: 'Elevation', field: 'elevation'},
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

  @Input() set messageToGeocacheDetail(value: string) {
    this.http
      .get<any[]>('http://' + APP_CONFIG.host_url + '/uicache/geocachedetails/' + value).subscribe((data) => {
      this.agGrid.api.setRowData(data);
    });

  }
  constructor(private http: HttpClient) {}

  // Example load data from server
  onDetailGridReady(params: GridReadyEvent) {

  }
}
