import {Component, Input, OnInit} from '@angular/core';
import {ActionModel} from '../model/action.model';

@Component({
  selector: 'app-action-list',
  templateUrl: './action-list.component.html',
  styleUrls: ['./action-list.component.sass']
})
export class ActionListComponent implements OnInit {

  @Input() actions: Array<ActionModel>;

  constructor() {
  }

  ngOnInit(): void {
  }

}
