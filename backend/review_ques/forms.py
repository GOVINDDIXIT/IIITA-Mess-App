from django import forms
from .models import ReviewQuestion

class ChangeTimestampForm(forms.ModelForm):

    class Meta:
        model = ReviewQuestion
        fields = ['timestamp']
