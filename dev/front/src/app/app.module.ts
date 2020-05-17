import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {ActionListComponent} from './action-list/action-list.component';
import {ActionItemComponent} from './action-item/action-item.component';
import {TrambuLandingComponent} from './trambu-landing/trambu-landing.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatCardModule} from '@angular/material/card';
import { MatSliderModule } from '@angular/material/slider';
import {MatIconModule} from '@angular/material/icon';
import {MatExpansionModule} from '@angular/material/expansion';
import {MatGridListModule} from '@angular/material/grid-list';

@NgModule({
    declarations: [
        AppComponent,
        ActionListComponent,
        ActionItemComponent,
        TrambuLandingComponent,
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        BrowserAnimationsModule,
        MatSliderModule,
        MatCardModule,
        MatIconModule,
        MatExpansionModule,
        MatGridListModule
    ],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule {
}
