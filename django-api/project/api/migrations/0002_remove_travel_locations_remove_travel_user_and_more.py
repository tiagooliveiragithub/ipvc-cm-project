# Generated by Django 5.0.4 on 2024-04-13 12:14

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0001_initial'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='travel',
            name='locations',
        ),
        migrations.RemoveField(
            model_name='travel',
            name='user',
        ),
        migrations.RemoveField(
            model_name='userprofile',
            name='user',
        ),
        migrations.DeleteModel(
            name='Location',
        ),
        migrations.DeleteModel(
            name='Travel',
        ),
        migrations.DeleteModel(
            name='UserProfile',
        ),
    ]
