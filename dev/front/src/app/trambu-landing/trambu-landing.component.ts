import {Component, OnInit} from '@angular/core';
import {ActionModel} from '../model/action.model';

@Component({
    selector: 'app-trambu-landing',
    templateUrl: './trambu-landing.component.html',
    styleUrls: ['./trambu-landing.component.sass']
})
export class TrambuLandingComponent implements OnInit {

    actions = new Array<ActionModel>();
    defaultDesc = 'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry\'s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting';

    constructor() {
        this.actions.push(new ActionModel('Write a front end', new Date('20/05/2020'), this.defaultDesc));
        this.actions.push(new ActionModel('Connect front-end to backend', new Date('20/05/2020'), this.defaultDesc));
    }

    ngOnInit(): void {

    }

}
