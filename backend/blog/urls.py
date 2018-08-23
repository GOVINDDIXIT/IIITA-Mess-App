from django.urls import path

from . import views

urlpatterns = [
    path('', views.homepage, name='homepage'),
    path('view/<date>/', views.view_post, name='view_post'),
    path('previous_report/', views.previous_reports, name='previous_report'),
    path('developers/', views.developers, name='developers'),
    path('check-status/', views.today_report_submitted, name="check_status"),
]
