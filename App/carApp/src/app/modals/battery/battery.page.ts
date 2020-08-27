import { Component, ViewChild, ElementRef } from "@angular/core";
import { ModalController } from '@ionic/angular';
import { DataServiceService } from '../../service/data-service.service';
import { Subscription } from 'rxjs';
import { Chart } from 'chart.js';

@Component({
  selector: 'app-battery',
  templateUrl: 'battery.page.html',
  styleUrls: ['battery.page.scss']
})
export class BatteryPage {
  @ViewChild("batteryCanvas", { static: true }) barCanvas: ElementRef;

  private batteryChart: Chart;
  values: any[] = [];
  subscription: Subscription;
  xAxisLabels: any[] = [];
  xAxis: String = "Time";
  yAxis: String = "Battery %";

  constructor(private modalContrller: ModalController, private service: DataServiceService) {
    this.subscription = this.service.checkData().subscribe((data) => {
      this.values = [];
      this.xAxisLabels = [];
      data.forEach(element => {
        this.xAxisLabels.push(new Date(element[0]).getHours() + " : " + new Date(element[0]).getMinutes());
        this.values.push(element[5]); //5
      });

      // Delete all data
      this.batteryChart.data.labels = [];
      this.batteryChart.data.datasets[0].data = [];

      // Add all data
      this.batteryChart.data.labels = this.xAxisLabels;
      this.batteryChart.data.datasets[0].data = this.values;


      this.batteryChart.update();
    });
  }


  ionViewDidEnter() {
    this.drawBatteryChart();

  }

  getGasLevel() {
    if (this.values.length > 0)
      return this.values[this.values.length - 1];
    else return 0;
  }

  drawBatteryChart() {
    let canvas: any = document.getElementById("batteryCanvas");
    let ctx = canvas.getContext("2d");
    var gradient = ctx.createLinearGradient(0, 0, 0, 300);
    gradient.addColorStop(0.2, 'rgba(40,175,250,.25)');
    gradient.addColorStop(1, 'rgba(40,175,250,0)');
    var gridgradient = ctx.createLinearGradient(0, 0, 500, 0);
    gridgradient.addColorStop(0.1, 'rgba(200,200,250,0)');
    gridgradient.addColorStop(0.5, 'rgba(200,200,250,0.12)');
    gridgradient.addColorStop(0.9, 'rgba(200,200,250,0 )');

    this.batteryChart = new Chart(this.barCanvas.nativeElement, {

      type: "line",
      options: {
        animation: {
          duration: 1000,
          easing: 'easeInCubic',
        },
        responsive: true,
        legend: {
          display: false,
        },
        maintainAspectRatio: false,
        scales: {

          yAxes: [{
            gridLines: {
              display: true,
              color: gridgradient
            },
            ticks: {
              callback: function (value, index, values) {
                return value + '%';
              },
              beginAtZero: true,
              autoSkipPadding: 50,
              precision: 0,
              autoSkip: true,
            },
            scaleLabel: {
              display: false,
            }
          }],
          xAxes: [{
            gridLines: {
              display: true,
              color: gridgradient
            },
            ticks: {
              autoSkip: true,
              maxTicksLimit: 5
            },
            offset: true,
            scaleLabel: {
              display: false,
              labelString: this.xAxis
            }
          }]
        }
      },
      data: {
        labels: this.xAxisLabels,
        datasets: [
          {
            fill: true,
            backgroundColor: gradient,
            borderColor: "#28AFFA",
            borderCapStyle: "butt",
            pointRadius: 0,
            data: this.values,
            // spanGaps: true
          }
        ]
      }
    });
  }
  
  getGasPercentage() {
    let dasharray = 600;
    if (this.values.length > 0) {
      let gas = this.values[this.values.length - 1];
      return dasharray - (dasharray * gas) / 100;
    }
    else return dasharray;
  }

  dismiss() {
    this.subscription.unsubscribe();
    this.modalContrller.dismiss();
  }


}