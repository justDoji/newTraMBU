import {Component, Input, OnInit} from '@angular/core';
import {ActionModel} from '../model/action.model';

@Component({
    selector: 'app-action-item',
    templateUrl: './action-item.component.html',
    styleUrls: ['./action-item.component.sass']
})
export class ActionItemComponent implements OnInit {

    @Input() action: ActionModel;

    constructor() {
    }

    ngOnInit(): void {
    }

}
