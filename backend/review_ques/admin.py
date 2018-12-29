from django.contrib import admin
from django.http import HttpResponseRedirect
from django.shortcuts import render
from .forms import ChangeTimestampForm

from . import models


class ReviewQuestionAdmin(admin.ModelAdmin):

    list_display = ['ques', 'check', 'timestamp', 'ques_type']
    actions = ['change_timestamp']
    readonly_fields = ['timestamp']

    def change_timestamp(self, request, queryset):
        if 'new_timestamp' in request.POST:
            form = ChangeTimestampForm(request.POST)
            if form.is_valid():
                timestamp = form.cleaned_data.get('timestamp')
                queryset.update(timestamp=timestamp)
            self.message_user(
                request,
                'Change date-time field for {} items.'.format(queryset.count())
            )
            return HttpResponseRedirect(request.get_full_path())
        form = ChangeTimestampForm()
        return render(
            request,
            'review_ques/change_timestamp.html',
            context={'ques': queryset, 'form': form}
        )

    change_timestamp.short_description = 'Change Timestamp'


admin.site.register(models.ReviewQuestion, ReviewQuestionAdmin)
