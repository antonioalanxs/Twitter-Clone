import { RouterModule } from '@angular/router';

import { ExplorerComponent } from './screens/explorer/explorer.component';
import { LoginComponent } from './screens/form/login/login.component';
import { SignUpComponent } from './screens/form/signup/signup.component';
import { VerificationComponent } from './screens/form/signup/verification/verification.component';
import { ErrorComponent } from './screens/error/error.component';
import { ConfirmationComponent } from './screens/form/forgot-password/forgot-password-confirmation/forgot-password-confirmation.component';
import { ForgotPassword } from './screens/form/forgot-password/forgot-password.component';
import { ProfileComponent } from './screens/profile/profile.component';
import { ResetPassword } from './screens/form/reset-password/reset-password.component';
import { ResetPasswordConfirmation } from './screens/form/reset-password/reset-password-confirmation/reset-password-confirmation.component';
import { DashboardComponent } from './screens/dashboard/dashboard.component';
import { SearchComponent } from './screens/search/search.component';
import { HomeComponent } from './screens/home/home.component';
import { IndexComponent } from "./screens/index/index.component";
import { BookmarksComponent } from './screens/bookmarks/bookmarks.component';
import { NotificationsComponent } from './screens/notifications/notifications.component';
import { WriteTweetComponent } from './screens/write-tweet/write-tweet.component';
import { ShowTweetComponent } from './screens/show-tweet/show-tweet.component';
import { FollowersComponent } from './screens/followers/followers.component';
import { EditProfileComponent } from './screens/profile/edit-profile/edit-profile.component';

const appRoutes = [
  {
    path: '',
    component: IndexComponent,
    title: 'Twitter'
  },
  {
    path: 'bookmarks',
    component: BookmarksComponent,
    title: 'Bookmarks / Twitter'
  },
  {
    path: 'explore',
    component: ExplorerComponent,
    title: 'Explore / Twitter'
  },
  {
    path: 'login',
    component: LoginComponent,
    title: 'Login / Twitter'
  },
  {
    path: 'signup',
    component: SignUpComponent,
    title: 'Signup / Twitter'
  },
  {
    path: 'verification',
    component: VerificationComponent,
    title: 'Verification / Twitter'
  },
  {
    path: 'error',
    component: ErrorComponent,
    title: 'Error / Twitter'
  },
  {
    path: 'forgot-password-confirmation',
    component: ConfirmationComponent,
    title: 'Forgot password confirmation / Twitter'
  },
  {
    path: 'forgot-password',
    component: ForgotPassword,
    title: 'Forgot password / Twitter'
  },
  {
    path: 'reset-password/:passwordToken',
    component: ResetPassword,
    title: 'Reset your password / Twitter'
  },
  {
    path: 'reset-password-confirmation',
    component: ResetPasswordConfirmation,
    title: 'Reset password confirmation / Twitter'
  },
  {
    path: 'profile/:id',
    component: ProfileComponent,
    title: 'Profile / Twitter'
  },
  {
    path: 'profile/:id/edit-profile',
    component: EditProfileComponent,
    title: 'Edit profile / Twitter'
  },
  {
    path: 'profile/:id/:users-list',
    component: FollowersComponent,
    title: 'People related / Twitter'
  },
  {
    path: 'dashboard',
    component: DashboardComponent,
    title: 'Dashboard / Twitter'
  },
  {
    path: 'search/:keyword',
    component: SearchComponent,
    title: 'Explore / Twitter'
  },
  {
    path: 'home',
    component: HomeComponent,
    title: 'Home / Twitter'
  },
  {
    path: 'notifications',
    component: NotificationsComponent,
    title: 'Notifications / Twitter'
  },
  {
    path: 'write-tweet',
    component: WriteTweetComponent,
    title: 'Write a tweet / Twitter'
  },
  {
    path: 'write-tweet/comment/:id',
    component: WriteTweetComponent,
    title: 'Comment a tweet / Twitter'
  },
  {
    path: 'tweet/:id',
    component: ShowTweetComponent,
    title: 'Tweet / Twitter'
  },
]

export const AppRoutingModule = RouterModule.forRoot(appRoutes);
