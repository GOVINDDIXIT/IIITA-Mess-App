from django.contrib import admin

from .models import Review


class ReviewAdmin(admin.ModelAdmin):
    readonly_fields = ('timestamp',)
    list_display = ['user', 'ques', 'rating', 'timestamp']


admin.site.register(Review, ReviewAdmin)
