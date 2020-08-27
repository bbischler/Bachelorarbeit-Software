import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DataServiceService {

  // url: string = 'http://localhost:8080/cars/get/tesla1/20';
  url: string = 'http://pp3-quarkus-send-pp3.apps.us-east-2.starter.openshift-online.com/cars/get/tesla0/10';

  labels: string[][];
  values: any[] = [];
  timeout: number = 5000;
  isTesting: boolean = true;
  testData: any = [["2020-04-16T08:02:03.926Z", 1.62, 2.69, 1.22, 2.24, 75.5, 85.55, 140.72],
  ["2020-04-16T08:02:03.926Z", 1.62, 2.69, 1.22, 2.24, 73.5, 85.55, 140.72]];

  constructor(private httpclient: HttpClient) {
  }

  /**
  * Gets data every second and passes the data to the subscribers
  *
  *@return SensorValues
  */
  checkData() {
    return Observable.create(observer => {
      setInterval(() => {
        if (this.isTesting) {
          observer.next(this.testData)
        } else {
          this.fetchData().subscribe(data => {
            // let values = data["results"][0]["series"][0]["columns"]
            let values: any[] = [];
            values = data["results"][0]["series"][0]["values"];
            console.log(values.reverse());
            observer.next(values.reverse());
          });
        }
      }, this.timeout)
    })
  }

  /**
  * Fetches data from the backend
  *
  *@return SensorValues
  */
  fetchData() {
    let headers = new HttpHeaders();
    headers.append('Content-Type', 'application/json');
    return this.httpclient.get<any[]>(this.url, { headers: headers });
  }
}
