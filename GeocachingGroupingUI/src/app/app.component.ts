@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

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
