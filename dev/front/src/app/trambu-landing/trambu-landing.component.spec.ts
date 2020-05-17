import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TrambuLandingComponent } from './trambu-landing.component';

describe('TrambuLandingComponent', () => {
  let component: TrambuLandingComponent;
  let fixture: ComponentFixture<TrambuLandingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TrambuLandingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TrambuLandingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
