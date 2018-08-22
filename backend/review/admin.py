from django.contrib import admin

from . import models


class ReviewQuestionAdmin(admin.ModelAdmin):

    list_display = ['ques', 'check', 'timestamp', 'ques_type']


admin.site.register(models.ReviewQuestion, ReviewQuestionAdmin)
