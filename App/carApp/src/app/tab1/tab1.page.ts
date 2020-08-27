import { Component } from '@angular/core';
import { DataServiceService } from '../service/data-service.service';

import { ModalController } from '@ionic/angular';
import { BatteryPage } from '../modals/battery/battery.page';
import { Subscription } from 'rxjs';


@Component({
  selector: 'app-tab1',
  templateUrl: 'tab1.page.html',
  styleUrls: ['tab1.page.scss']
})
export class Tab1Page {
  values: any[] = [];
  subscription: Subscription;
  constructor(private service: DataServiceService, private modalCtrl: ModalController) {
    this.subscription = this.service.checkData().subscribe(data => {
      this.values = [];
      this.values = data;
    });
  }

  /**
  * creates a modal to display battery data
  *
  */
  async presentModalBattery() {
    const modal = await this.modalCtrl.create({
      component: BatteryPage,
      componentProps: { value: 123 }
    });
    return await modal.present();
  }

  /**
  * returns the last battery value
  *
  *@return number
  */
  getCurrentBattery() {
    if (this.values.length > 0)
      return this.values[this.values.length - 1][5];
    else return 0;
  }

  /**
  * returns the last totalMileage value
  *
  *@return number
  */
  getTotalMileage() {
    if (this.values.length > 0)
      return this.values[this.values.length - 1][7];
    else return 0;
  }

  /**
  * returns the last airPressure value
  *
  *@return number
  */
  getAirPressure(num: number) {
    if (this.values.length > 0)
      return this.values[this.values.length - 1][num];
    else return 0;
  }

  /**
  * returns the airPressure in percentage
  *
  *@return number
  */
  getTirePercentage(num: number) {
    let maxBar = 3;
    let dasharray = 200;
    if (this.values.length > 0) {
      let bar = this.values[this.values.length - 1][num];
      let prozent = bar / maxBar * 100;
      return dasharray - (dasharray * prozent) / 100;
    }
    else return dasharray;
  }

  /**
  * If the page is closed, the subscription to fetchData() will be canceled
  *
  *@return number
  */
  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

}
