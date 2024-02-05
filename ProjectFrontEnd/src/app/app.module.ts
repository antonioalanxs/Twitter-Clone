import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms'
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgxChartsModule } from '@swimlane/ngx-charts';

import { AppRoutingModule } from './app.routing';

import { ThemeSwitcherButtonComponent } from './commons/theme-switcher-button/theme-switcher-button.component';
import { SearcherComponent } from './commons/searcher/searcher.component';
import { TweetComponent } from './entities/tweet/tweet.component';
import { ExplorerComponent } from './screens/explorer/explorer.component';
import { TrendComponent } from './entities/trend/trend.component';
import { NotificationComponent } from './entities/notification/notification.component';
import { TweetListComponent } from './entities/tweet/tweet-list/tweet-list.component';
import { UserComponent } from './entities/user/user.component';
import { ProfileComponent } from './screens/profile/profile.component';
import { LoginComponent } from './screens/form/login/login.component';
import { SignUpComponent } from './screens/form/signup/signup.component';
import { VerificationComponent } from './screens/form/signup/verification/verification.component';
import { ErrorComponent } from './screens/error/error.component';
import { ConfirmationComponent } from './screens/form/forgot-password/forgot-password-confirmation/forgot-password-confirmation.component';
import { ForgotPassword } from './screens/form/forgot-password/forgot-password.component';
import { ResetPassword } from './screens/form/reset-password/reset-password.component';
import { ResetPasswordConfirmation } from './screens/form/reset-password/reset-password-confirmation/reset-password-confirmation.component';
import { SearchComponent } from './screens/search/search.component';
import { NotificationsComponent } from './screens/notifications/notifications.component';
import { DashboardComponent } from './screens/dashboard/dashboard.component';
import { HomeComponent } from './screens/home/home.component';
import { IndexComponent } from './screens/index/index.component';
import { BookmarksComponent } from './screens/bookmarks/bookmarks.component';
import { WriteTweetComponent } from './screens/write-tweet/write-tweet.component';
import { LeftBarComponent } from './commons/left-bar/left-bar.component';
import { NotificationListComponent } from './entities/notification/notification-list/notification-list.component';
import { ShowTweetComponent } from './screens/show-tweet/show-tweet.component';
import { FollowersComponent } from './screens/followers/followers.component';
import { EditProfileComponent } from './screens/profile/edit-profile/edit-profile.component';
import { RightBarComponent } from './commons/right-bar/right-bar.component';
import { TrendListComponent } from './entities/trend/trend-list/trend-list.component';
import { MobileBarComponent } from './commons/mobile-bar/mobile-bar.component';

@NgModule({
  declarations: [
    AppComponent,
    ThemeSwitcherButtonComponent,
    SearcherComponent,
    TweetComponent,
    ExplorerComponent,
    TrendComponent,
    NotificationComponent,
    TweetListComponent,
    UserComponent,
    ProfileComponent,
    LoginComponent,
    SignUpComponent,
    VerificationComponent,
    ErrorComponent,
    ConfirmationComponent,
    ForgotPassword,
    ResetPassword,
    ResetPasswordConfirmation,
    SearchComponent,
    DashboardComponent,
    NotificationsComponent,
    IndexComponent,
    BookmarksComponent,
    HomeComponent,
    WriteTweetComponent,
    LeftBarComponent,
    NotificationListComponent,
    ShowTweetComponent,
    FollowersComponent,
    EditProfileComponent,
    RightBarComponent,
    TrendListComponent,
    MobileBarComponent,
  ],
  imports: [
    FormsModule,
    HttpClientModule,
    NgxChartsModule,
    BrowserAnimationsModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})

export class AppModule { }
