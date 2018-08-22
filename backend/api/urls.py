from django.urls import path, include

urlpatterns = [
    path('review/', include('api.review.urls')),
]
